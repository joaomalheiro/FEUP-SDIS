# FEUP-SDIS

# To compile the code : 

javac -d bin -sourcepath src src/testapp/TestApp.java src/peer/Peer.java

# To run a peer : 

java -classpath bin peer.Peer <protocol_version> <server_id> <service_ap> <mc_ip> <mc_port> <mdb_ip> <mdb_port> <mdr_ip> <mdr_port>

E.G : java -classpath bin peer.Peer 1.0 1 1 230.0.0.0 4446 225.0.0.0 5000 228.0.0.0 4450

E.G2: java -classpath bin peer.Peer 1.0 2 2 230.0.0.0 4446 225.0.0.0 5000 228.0.0.0 4450

# To run the testapp :

java -classpath bin testapp.TestApp <peer_access_point> BACKUP <file_name> <desired_replication_degree>

E.G : java -classpath bin testapp.TestApp 2 BACKUP testflower.jpg 2


java -classpath bin testapp.TestApp <peer_access_point> RESTORE <file_name> 

E.G : java -classpath bin testapp.TestApp 2 RESTORE testflower.jpg


java -classpath bin testapp.TestApp <peer_access_point> DELETE <file_name> 

E.G : java -classpath bin testapp.TestApp 2 DELETE testflower.jpg


ava -classpath bin testapp.TestApp <peer_access_point> RECLAIM <max_amount_disk_space>

E.G : java -classpath bin testapp.TestApp 2 RECLAIM 1000


java -classpath bin testapp.TestApp <peer_access_point> STORE

E.G : java -classpath bin testapp.TestApp 2 STORE
