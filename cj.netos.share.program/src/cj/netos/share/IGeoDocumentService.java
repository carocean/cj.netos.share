package cj.netos.share;

import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface IGeoDocumentService {

    GeoReceptor getReceptor(String id) throws CircuitException;

    GeosphereDocument getGeoDocument(String docid) throws CircuitException;

    List<GeoDocumentMedia> listExtraMedia(String docid) throws CircuitException;

    List<GeoDocumentLike> pageLike(String docid, long limit, long skip) throws CircuitException;

    List<GeoDocumentComment> pageComment(String docid, long limit, long skip) throws CircuitException;

    long getLikeCount(String docid) throws CircuitException;

    long getCommendCount(String docid) throws CircuitException;
}
