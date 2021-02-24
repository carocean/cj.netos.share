package cj.netos.share.webview;

import cj.netos.share.*;
import cj.netos.share.model.AppAccount;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.ultimate.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CjService(name = "/pages/netflow-viewer.html")
public class NetflowDocumentWebview implements IGatewayAppSiteWayWebView {
    @CjServiceRef
    INetflowDocumentService netflowDocumentService;
    @CjServiceRef
    IPersonService personService;
    @CjServiceRef
    IAccessTokenService accessTokenService;

    @Override
    public void flow(Frame frame, Circuit circuit, IGatewayAppSiteResource resource) throws CircuitException {
        String docid = frame.parameter("docid");
        if (StringUtil.isEmpty(docid)) {
            throw new CircuitException("404", "缺少参数：docid");
        }
        String creator = frame.parameter("creator");
        if (StringUtil.isEmpty(creator)) {
            throw new CircuitException("404", "缺少参数：creator");
        }
        AccessTokenInfo accessTokenInfo = accessTokenService.getInfo();
        ChannelDocument channelDoc = netflowDocumentService.getDocument(creator, docid);
        Document document = resource.html("/pages/viewer.html");
        printDocument(accessTokenInfo, channelDoc, document);
        circuit.content().writeBytes(document.html().getBytes());
    }

    private void printDocument(AccessTokenInfo accessTokenInfo, ChannelDocument doc, Document e) throws CircuitException {
        e.select(".demo p.fx_content").html(doc.getContent());
        AppAccount account = personService.getPerson(doc.getCreator());
        NetflowChannel channel = netflowDocumentService.getChannel(doc.getCreator(), doc.getChannel());
        printAvatar(accessTokenInfo, channel, account, e);
        Element ul = e.select(".my-gallery").first();
        printMedias(accessTokenInfo, doc, ul, doc.getCreator());
        printSubject(accessTokenInfo, doc, channel, account, e);
        printExtra(accessTokenInfo, doc, channel, account, e, doc.getCreator());
    }

    private void printExtra(AccessTokenInfo accessTokenInfo, ChannelDocument doc, NetflowChannel channel, AppAccount account, Document e, String creator) throws CircuitException {
        String accessToken = accessTokenInfo.getAccessToken();
        String nonce = accessTokenInfo.getNonce();
        List<NetflowDocumentLike> likes = netflowDocumentService.pageExtraLike(creator, doc.getId(), 20, 0);
        List<NetflowDocumentComment> comments = netflowDocumentService.pageExtraComment(creator, doc.getId(), 30, 0);
        Element likesE = e.select(".likes").first();
        Element commendsE = e.select(".commends").first();
        long likeCount = netflowDocumentService.getLikeCount(creator,doc.getId());
        long commendCount = netflowDocumentService.getCommendCount(creator,doc.getId());
        if (likes.isEmpty()) {
            likesE.remove();
        } else {
            likesE.select("span").remove();
            List<String> list = new ArrayList<>();
            for (NetflowDocumentLike details : likes) {
                list.add(details.getPerson());
            }
            Map<String, AppAccount> map = new HashMap<>();
            List<AppAccount> appAccounts = personService.listPerson(list);
            for (AppAccount appAccount : appAccounts) {
                map.put(appAccount.getAccountId(), appAccount);
            }
            for (NetflowDocumentLike details : likes) {
                AppAccount appAccount = map.get(details.getPerson());
                if (appAccount == null) {
                    continue;
                }
                likesE.append(String.format("<span>%s</span>", appAccount.getNickName()));
            }
            if (likes.size() > 20) {
                likesE.append(String.format("<span>等共%s人</span>", likeCount));
            }
        }
        if (comments.isEmpty()) {
            commendsE.remove();
        } else {
            Element li = commendsE.select("li").first().clone();
            commendsE.empty();
            List<String> list = new ArrayList<>();
            for (NetflowDocumentComment details : comments) {
                list.add(details.getPerson());
            }
            Map<String, AppAccount> map = new HashMap<>();
            List<AppAccount> appAccounts = personService.listPerson(list);
            for (AppAccount appAccount : appAccounts) {
                map.put(appAccount.getAccountId(), appAccount);
            }

            for (NetflowDocumentComment details : comments) {
                AppAccount appAccount = map.get(details.getPerson());
                if (appAccount == null) {
                    continue;
                }
                Element cli = li.clone();
                cli.select(".nick").html(appAccount.getNickName());
                cli.select(".commend").html(details.getContent() == null ? "" : details.getContent());
                cli.select(".time").html(DateUtils.convertTimeToFormat(details.getCtime()));
                commendsE.appendChild(cli);
            }
            if (comments.size() > 30) {
                commendsE.append(String.format("<li style='padding-top:10px;text-align:right;font-size:12px;'><span>等共%s人评论</span></li>", commendCount));
            }
        }
    }

    private void printSubject(AccessTokenInfo accessTokenInfo, ChannelDocument doc, NetflowChannel channel, AppAccount appAccount, Document e) {
        String accessToken = accessTokenInfo.getAccessToken();
        String nonce = accessTokenInfo.getNonce();
        String ctime = DateUtils.convertTimeToFormat(doc.getCtime());
        Element subject = e.select(".subject").first();
        subject.select(".ctime").html(ctime);

        Element creator = subject.select(".creator").first();
        if (appAccount == null) {
            creator.remove();
        } else {
            creator.select("span").html(appAccount.getNickName());
            creator.select("img").attr("src", String.format("%s?accessToken=%s&nonce=%s", appAccount.getAvatar(), accessToken, nonce));
        }
    }

    private void printMedias(AccessTokenInfo accessTokenInfo, ChannelDocument doc, Element ul, String creator) throws CircuitException {
        List<NetflowMedia> medias = netflowDocumentService.listExtraMedia(creator, doc.getId());
        if (medias.isEmpty()) {
            ul.remove();
            return;
        }
        String accessToken = accessTokenInfo.getAccessToken();
        String nonce = accessTokenInfo.getNonce();
        if (medias.size() == 1) {
            NetflowMedia media = medias.get(0);
            if ("share".equals(media.getType())) {
                Element share = ul.select("share").first().clone();
                ul.empty();
                String leading = media.getLeading();
                int pos = leading.indexOf("?");
                if (pos < 0) {
                    leading = String.format("%s?accessToken=%s&nonce=%s", leading, accessToken, nonce);
                } else {
                    leading = String.format("%s&accessToken=%s&nonce=%s", leading, accessToken, nonce);
                }
                share.select("a").attr("href", media.getSrc());
                share.select("img").attr("src", leading);
                share.select("span").html(media.getText());
                ul.appendChild(share);
                return;
            }
            if ("video".equals(media.getType())) {
                Element video = ul.select("video").first().clone();
                ul.empty();
                String src = media.getSrc();
                int pos = src.indexOf("?");
                if (pos < 0) {
                    src = String.format("%s?accessToken=%s&nonce=%s", src, accessToken, nonce);
                } else {
                    src = String.format("%s&accessToken=%s&nonce=%s", src, accessToken, nonce);
                }
                Element source = video.select("source").first().clone();
                video.empty();
                source.attr("src", src);
                video.appendChild(source);
                video.appendText("不支持的视频格式");
                ul.appendChild(video);
                return;
            }
        }
        Element cfigure = ul.select("figure").first().clone();
        ul.empty();
        for (NetflowMedia media : medias) {
            Element figure = cfigure.clone();
            String src = String.format("%s?accessToken=%s&nonce=%s", media.getSrc(), accessToken, nonce);
            figure.select("a").attr("href", src);
            figure.select("img").attr("src", src);
            ul.appendChild(figure);
        }
    }

    private void printAvatar(AccessTokenInfo accessTokenInfo, NetflowChannel channel, AppAccount account, Document e) {
        e.select(".title span").html(channel.getTitle());
        String leading = channel.getLeading();
        if (StringUtil.isEmpty(leading)) {
            e.select(".header img").attr("src", "../images/netflow.png");
        } else {
            e.select(".header img").attr("src", String.format("%s?accessToken=%s&nonce=%s", leading, accessTokenInfo.getAccessToken(), accessTokenInfo.getNonce()));
        }
    }
}
