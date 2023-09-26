# 1Fichier-Java-API

Java Integration of 1Fichier website's API

Jar file can be found in files above : 1FichierAPI.jar

# Create Instance

    FichierAPI instance = new FichierAPI( YOUR BEARER_TOKEN );

# Send Request

With the newer update, you can both send Get and Post requests.

The method to achieve It is :

    instance.makeRequest(Request req, FastMap parameters);

Request is an Interface that is implemented by GetRequest and PostRequest enums.

# Create Json parameters : FastMap

FastMap is a map designed to work with Json and and vice versa. 
FastMap value is an Object, that can be String, Integer, Array, and even FastMap itself !

There are two ways to create a FastMap :

- Static way :

      FastMap parameters = FastMap.of("key1", value1, "key2", value2, ...);

- Classic way :

      FastMap parameters = new FastMap((short) 1); // Maximum number of parameters
      parameters.insert("key1", value);

# Examples using 1Fichier API

You can check 1Fichier Website's API here : https://api.1fichier.com/

- Delete Files

      FastMap parameters = FastMap.of(
        "files", new FastMap[] { 
          FastMap.of("url", "https://1fichier.com/?exemple",
          FastMap.of("url", "https://1fichier.com/?exemple"
        }
      );

      FastMap result = instance.postRequest(PostRequest.DELETE_FILES, parameters); // In a try catch block

- Move Files

      FastMap parameters = FastMap.of(
        "urls", new String[] { 
          "https://1fichier.com/?exemple",
          "https://1fichier.com/?exemple"
        },
        "destination_folder_id", 0
      );

      FastMap result = instance.postRequest(PostRequest.MOVE_FILES, parameters); // In a try catch block

- Upload a Unique File

        FastMap parameters = new FastMap((short) 2);
        parameters.insert("did", 0); // Upload at root
        parameters.insert("dpass", "w12345678x"); // Password to access files
        
        FastMap[] result = instance.upload(parameters, 
                new FastMap[] { 
                    FastMap.of("file_name", "test.txt", "file_path", new File("test.txt").toPath() 
                };

        String fileLink = result[0].get("file_url");

- Upload Files & Folders (New!)

For this example, we take a folder that represents a minecraft server :

        $ ls D:/serv
        banned-ips.json      logs/                                  spigot.yml
        banned-players.json  ops.json                               usercache.json
        bukkit.yml           permissions.yml                        whitelist.json
        commands.yml         plugins/                               world/
        eula.txt             server.properties                      world_nether/
        help.yml             spigot-1.8.8-R0.1-SNAPSHOT-latest.jar  world_the_end/
        
        $ ls D:/serv/world
        data/  level.dat  playerdata/  region/  session.lock  uid.dat

The Full Working class to upload this folder can be found in examples/FichierUploader.java

Here is the output that I obtained :

    {"file_name":"banned-ips.json","file_length":"2 o","file_url":"https://1fichier.com/?vm9zurd09cp0she7rk51","delete_file_url":"--"}
    {"file_name":"banned-players.json","file_length":"2 o","file_url":"https://1fichier.com/?2zsyevrhakxdn1bbyj6s","delete_file_url":"--"}
    {"file_name":"bukkit.yml","file_length":"1.15 Ko","file_url":"https://1fichier.com/?2fjhpnhd54lc8eu4y5as","delete_file_url":"--"}
    {"file_name":"commands.yml","file_length":"560 o","file_url":"https://1fichier.com/?kc2oea2mjlohw7obrb8y","delete_file_url":"--"}
    {"file_name":"eula.txt","file_length":"184 o","file_url":"https://1fichier.com/?6jrupn1wqgvbhjikrh04","delete_file_url":"--"}
    {"file_name":"help.yml","file_length":"2.52 Ko","file_url":"https://1fichier.com/?jy4qp5x80417jswc2104","delete_file_url":"--"}
    ...
