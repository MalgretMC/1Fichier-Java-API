# 1Fichier-Java-API

Java Integration of 1Fichier website's API

Jar file can be found in files above : 1FichierAPI.jar

# Create Instance

    FichierAPI instance = new FichierAPI( YOUR BEARER_TOKEN );

# Send post Request

For the moment, only post premium request are available

    instance.postRequest(PostRequest req, FastMap parameters);

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
