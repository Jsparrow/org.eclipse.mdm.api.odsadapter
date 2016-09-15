# MDM5 API #


This document describes the services defined in the MDM5 API.


## 1. Introduction ##

The MDM5 API consists of the following projects:


-   **org.eclipse.mdm.api.base**\
    Defines the core services and entities. Entities contained in this
    project must be supported by all implementations.

-   **org.eclipse.mdm.api.default**\
    Extends the core API with catalog and template entities to organize
    the storage of context data.

-   **org.eclipse.mdm.api.odsadapter**\
    Implements both org.eclipse.mdm.api.base and org.eclipse.mdm.default
    using the query based ASAM ODS OO API.


## 2. EntityManager ##

Once a connection to a persistence (e.g. ODS server) is established, the
entity manager allows to load entities of any kind and provides access
to available services.


Returned entities are always complete. This means they are loaded with
all of their properties and related entities (as exposed by their API).
Therefore it does not matter how entities are retrieved (e.g. entity
manager or search service). The entity manager provides various methods
to load instances by their id, load parent or child entities for a given
entity or filter loaded entities with a given naming pattern. Context
data may be loaded for TestStep or Measurement entities.


Mass data may be loaded by defining a corresponding request. Such a
request consists of a ChannelGroup and a subset of related Channels.
Finally, a range is defined to loaded stored values iteratively or all at
once. This allows a fine grained access to stored mass data.


### 2.1. SearchService ###

The search service allows to search for entities like Test, TestStep,
Measurement and Channel across hierarchies. So one can, for example,
search for Tests and apply filter criteria for context data of related
TestSteps and Measurements without defining any joins. This is done by
the internally used search queries. Each search query defines a set of
supported entity types and hence all of its attributes may be used for
selections or filter criteria. The search service allows to query
possible filter values for each supported attribute.


In addition to that a query string may be used to search for entities.
This query string is evaluatated and processed by a full text search
which returns a result consisting of Test, TestStep or Measurement
entities.


### 2.2. Notification ###

TODO


### 2.3. FileService ###

The file service may be used to download externally linked files, query
their size or open a consumable download stream for a given externally
linked file. Sequential or parallel download of multiple files is
possible as well. A download request with multiple files may have
multiple links to the same file. In such a case this file is downloaded
only once and all links will point to the same local copy of the
downloaded file. Since downloading large files may take its time to
complete, one can pass a listener to track the overall progress.

NOTE:
<pre>
This service does not allow manually uploading or deleting files. Instead 
files are automatically uploaded / deleted while entities are written 
within a Section 2.5, "Transaction".
</pre>

### 2.4. EntityFactory ###

The entity factory is the only way to create new entities. Each entity
returned by this service is considered to be virtual, since it does not
have an id until it is written. Entities with informational relations
like Quantity (references a Unit) or Unit (references a
PhysicalDimension) may only reference already persisted entities. This
means that a new Quantity may only be created with an already persisted
Unit and a new Unit may only be created with an already persisted
PhysicalDimension.


On the other hand, it is possible to create parent / child trees
consisting of virtual or persisted entities (parent and child entities
are internally linked together). Within a transaction such trees are
recursively processed, therefore it is not required to explicitly
create, update or delete child entities, instead it is sufficient and
highly recommended to write only the root entity of the tree(s). The
transaction service recursively resolves such trees and executes batch
insert-, update- or delete-statements for the children.


### 2.5. Transaction ###

The transaction service effectively modifies the stored contents.
Entities are grouped by their type before they are processed. Each group
is processed at once with a batch statement. Each written entity is
scanned for externally linked files. New ones are automatically uploaded,
while removed ones are deleted. In case of errors any changes made to the
persistence are cancelled and hence any successfully uploaded file has to
be deleted. Unfortunately, the removal of uploaded files cannot be
guaranteed (e.g. a broken connection), instead a delete request for the
uploaded files is send on a best effort basis.

IMPORTANT:
<pre>
Externally linked files of deleted entities are only removed if the
entity is an instance of FilesAttachable (Test, TestStep, Measurement).
In any other case, externally linked files are not removed since this 
may have an impact on other entities which reference the same file.
</pre>

Besides the modification of entities, it is possible to write mass data
by defining write requests. A write request describes in detail how the
mass data of a Channel is organized (e.g. values are explicit or
generated, stored in externally linked files etc.).



## 3. Connect to an ODS Server ##

The ODS entity manager factory is used to connect to an ODS server. It
takes parameters, shown in the example below, to establish a
connection. On success an entity manager is returned.


```java
Map<String, String> connectionParameters = new HashMap<>();
connectionParameters.put(ODSEntityManagerFactory.PARAM_NAMESERVICE, "corbaloc::1.2@<SERVER>:<PORT>/NameService");
connectionParameters.put(ODSEntityManagerFactory.PARAM_SERVICENAME, "<SERVICE>.ASAM-ODS");
connectionParameters.put(ODSEntityManagerFactory.PARAM_USER, "sa");
connectionParameters.put(ODSEntityManagerFactory.PARAM_PASSWORD, "sa");

EntityManager entityManager = new ODSEntityManagerFactory().connect(connectionParameters);

// do something useful

entityManager.close();
```

Last updated 2016-09-14 07:35:33 MS