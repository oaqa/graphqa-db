graphqa-db
==========

This repository consists of a knowledge graph builder that extracts information from a given website URL and stores it in a graph structured format.

A sample invocation for this operation is provided below:-

KnowledgeGraphBuilder k = new KnowledgeGraphBuilder();
k.createGraph(domain, graphLocation);

where 'domain' is the website URL and 'graphLocation' is the location in the file system where the graph files are created.
