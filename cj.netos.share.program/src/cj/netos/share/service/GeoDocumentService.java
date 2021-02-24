package cj.netos.share.service;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.share.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "geoDocumentService")
public class GeoDocumentService extends AbstractService implements IGeoDocumentService {
    String _getDocumentColName() {
        return String.format("geo.receptor.docs");
    }

    String _getLikeColName() {
        return String.format("geo.receptor.likes");
    }

    String _getCommentColName() {
        return String.format("geo.receptor.comments");
    }

    String _getFollowColName() {
        return String.format("geo.receptor.follows");
    }

    String _getReceptorColName() {
        return String.format("geo.receptors");
    }

    String _getMediaColName() {
        return String.format("geo.receptor.medias");
    }

    @Override
    public GeoReceptor getReceptor(String id) throws CircuitException {
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple ?(colname) ?(clazz) where {'tuple.id':'?(id)'}");
        IQuery<GeoReceptor> query = home().createQuery(cjql);
        query.setParameter("colname", _getReceptorColName());
        query.setParameter("clazz", GeoReceptor.class.getName());
        query.setParameter("id", id);
        IDocument<GeoReceptor> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public GeosphereDocument getGeoDocument(String docid) throws CircuitException {
        String colname = _getDocumentColName();
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple %s %s where {'tuple.id':'%s'}",
                colname,
                GeosphereDocument.class.getName(),
                docid
        );
        IQuery<GeosphereDocument> query = home().createQuery(cjql);
        IDocument<GeosphereDocument> documentIDocument = query.getSingleResult();
        if (documentIDocument == null) {
            return null;
        }
        return documentIDocument.tuple();
    }

    @Override
    public List<GeoDocumentMedia> listExtraMedia(String docid) throws CircuitException {
        String colname = _getMediaColName();
        String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'tuple.docid':'%s'}",
                colname,
                GeoDocumentMedia.class.getName(),
                docid
        );
        IQuery<GeoDocumentMedia> query = home().createQuery(cjql);
        List<IDocument<GeoDocumentMedia>> docs = query.getResultList();
        List<GeoDocumentMedia> list = new ArrayList<>();
        for (IDocument<GeoDocumentMedia> doc : docs) {
            list.add(doc.tuple());
        }
        return list;
    }

    @Override
    public List<GeoDocumentLike> pageLike(String docid, long limit, long skip) throws CircuitException {
        String colname = _getLikeColName();
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}).limit(%s).skip(%s) from tuple %s %s where {'tuple.docid':'%s'}",
                limit,
                skip,
                colname,
                GeoDocumentLike.class.getName(),
                docid
        );
        IQuery<GeoDocumentLike> query = home().createQuery(cjql);
        List<IDocument<GeoDocumentLike>> docs = query.getResultList();
        List<GeoDocumentLike> list = new ArrayList<>();
        for (IDocument<GeoDocumentLike> doc : docs) {
            list.add(doc.tuple());
        }
        return list;
    }

    @Override
    public List<GeoDocumentComment> pageComment(String docid, long limit, long skip) throws CircuitException {
        String colname = _getCommentColName();
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}).limit(%s).skip(%s) from tuple %s %s where {'tuple.docid':'%s'}",
                limit,
                skip,
                colname,
                GeoDocumentComment.class.getName(),
                docid
        );
        IQuery<GeoDocumentComment> query = home().createQuery(cjql);
        List<IDocument<GeoDocumentComment>> docs = query.getResultList();
        List<GeoDocumentComment> list = new ArrayList<>();
        for (IDocument<GeoDocumentComment> doc : docs) {
            list.add(doc.tuple());
        }
        return list;
    }

    @Override
    public long getLikeCount(String docid) throws CircuitException {
        return home().tupleCount(_getLikeColName(),String.format("{'tuple.docid':'%s'}",docid));
    }

    @Override
    public long getCommendCount(String docid) throws CircuitException {
        return home().tupleCount(_getCommentColName(),String.format("{'tuple.docid':'%s'}",docid));
    }
}
