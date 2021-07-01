## Delta lake on Oracle Cloud Infrastructure Data Flow

Oracle Cloud Infrastructure (OCI) [Data Flow](https://www.oracle.com/in/big-data/data-flow/) is a fully managed Apache Spark service to perform processing tasks on extremely large data sets without infrastructure to deploy or manage. This enables rapid application delivery because developers can focus on app development, not infrastructure management.
<br>

Delta Lake is a storage layer that brings scalable, ACID transactions to Apache Spark and other big-data engines. See the [Delta Lake Documentation](https://docs.delta.io/latest/index.html) for details.

This repository intended to provide go to project setup for running delta lake workload on OCI Server Less Spark aka Dataflow

Steps to get started
* Clone the repository and build the artifect.
```
mvn clean install
```
* Create a sample application in Dataflow 
