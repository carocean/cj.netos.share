package cj.netos.share;

import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface INetflowDocumentService {
    ChannelDocument getDocument(String creator, String docid) throws CircuitException;

    List<NetflowDocumentLike> pageExtraLike(String creator, String docid, int limit, int offset) throws CircuitException;

    List<NetflowDocumentComment> pageExtraComment(String creator, String docid, int limit, int offset) throws CircuitException;

    List<NetflowMedia> listExtraMedia(String creator, String docid) throws CircuitException;

    NetflowChannel getChannel(String creator, String channel) throws CircuitException;

    long getLikeCount(String creator,String docid) throws CircuitException;

    long getCommendCount(String creator, String docid) throws CircuitException;
}
