package cj.netos.share;

import cj.netos.share.model.AppAccount;
import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface IPersonService {
    List<AppAccount> listPerson(List<String> persons)throws CircuitException;
    AppAccount getPerson(String person)throws CircuitException;
}
