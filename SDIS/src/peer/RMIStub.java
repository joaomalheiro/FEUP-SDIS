package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIStub extends Remote {

    void backupProtocol(String file, int replicationDeg) throws RemoteException;
    void restoreProtocol(String file) throws RemoteException;
    void deleteProtocol(String file) throws RemoteException;

}
