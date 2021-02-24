package cj.netos.share.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.share.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "netflowDocumentService")
public class NetflowDocumentService extends AbstractService implements INetflowDocumentService {

    @Override
    public ChannelDocument getDocument(String creator, String docid) throws CircuitException {
        ICube cube = cubePerson(creator);
        String cjql = String.format("select {'tuple':'*'} from tuple network.channel.documents %s where {'tuple.id':'%s'}",
                ChannelDocument.class.getName(),
                docid
        );
        IQuery<ChannelDocument> query = cube.createQuery(cjql);
        IDocument<ChannelDocument> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }
    @Override
    public List<NetflowDocumentLike> pageExtraLike(String creator, String docid, int limit, int offset) throws CircuitException {
        ICube cube = cubePerson(creator);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple network.channel.documents.likes %s where {'tuple.docid':'%s'}", limit, offset, NetflowDocumentLike.class.getName(), docid);
        IQuery<NetflowDocumentLike> query = cube.createQuery(cjql);
        List<IDocument<NetflowDocumentLike>> docs = query.getResultList();
        List<NetflowDocumentLike> likes = new ArrayList<>();
        for (IDocument<NetflowDocumentLike> doc : docs) {
            likes.add(doc.tuple());
        }
        return likes;
    }

    @Override
    public List<NetflowDocumentComment> pageExtraComment(String creator, String docid, int limit, int offset) throws CircuitException {
        ICube cube = cubePerson(creator);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple network.channel.documents.comments %s where {'tuple.docid':'%s'}", limit, offset, NetflowDocumentComment.class.getName(), docid);
        IQuery<NetflowDocumentComment> query = cube.createQuery(cjql);
        List<IDocument<NetflowDocumentComment>> docs = query.getResultList();
        List<NetflowDocumentComment> comments = new ArrayList<>();
        for (IDocument<NetflowDocumentComment> doc : docs) {
            comments.add(doc.tuple());
        }
        return comments;
    }

    @Override
    public List<NetflowMedia> listExtraMedia(String creator, String docid) throws CircuitException {
        ICube cube = cubePerson(creator);
        String cjql = String.format("select {'tuple':'*'} from tuple network.channel.documents.medias %s where {'tuple.docid':'%s'}", NetflowMedia.class.getName(), docid);
        IQuery<NetflowMedia> query = cube.createQuery(cjql);
        List<IDocument<NetflowMedia>> docs = query.getResultList();
        List<NetflowMedia> medias = new ArrayList<>();
        for (IDocument<NetflowMedia> doc : docs) {
            medias.add(doc.tuple());
        }
        return medias;
    }

    @Override
    public NetflowChannel getChannel(String creator, String channel) throws CircuitException {
        ICube cube = cubePerson(creator);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple channels %s where {'tuple.channel':'%s'}", NetflowChannel.class.getName(), channel);
        IQuery<NetflowChannel> query = cube.createQuery(cjql);
        IDocument<NetflowChannel> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public long getLikeCount(String creator, String docid) throws CircuitException {
        ICube cube = cubePerson(creator);
        return cube.tupleCount("network.channel.documents.likes",String.format("{'tuple.docid':'%s'}",docid));
    }

    @Override
    public long getCommendCount(String creator, String docid) throws CircuitException {
        ICube cube = cubePerson(creator);
        return cube.tupleCount("network.channel.documents.comments",String.format("{'tuple.docid':'%s'}",docid));
    }
}
