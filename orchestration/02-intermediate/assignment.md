# Kubernetes Intermediate: Take-Home Assignments

Complete the following assignments to reinforce your understanding of intermediate Kubernetes concepts.

## Assignment 1: StatefulSet with Persistent Storage

Create a StatefulSet for a database application with the following specifications:
- Use the `mysql:lts` image
- Create 3 replicas
- Configure persistent storage using PersistentVolumeClaims
- Each pod should have its own PVC
- Set up a headless service for the StatefulSet
- Configure proper initialization and readiness checks
- Create a ConfigMap for the MySQL configuration
- Use a Secret for the MySQL root password

Demonstrate that the data persists when pods are deleted and recreated.

## Assignment 2: Custom DaemonSet

Create a custom DaemonSet that runs on all nodes in your cluster:
- The DaemonSet should run a container that collects node metrics
- Use the `node-exporter` image or create your own custom image
- Configure the DaemonSet to run on all nodes, including master nodes (use tolerations)
- Set resource limits and requests
- Create a Service to expose the metrics
- Add appropriate labels and annotations

## Assignment 3: Ingress Controller and Rules

1. Install an Ingress Controller in your cluster (if not already present)
2. Create two simple web applications:
   - App1: A deployment with 2 replicas running nginx with a custom index page saying "App 1"
   - App2: A deployment with 2 replicas running nginx with a custom index page saying "App 2"
3. Create Services for both applications
4. Configure Ingress rules to route traffic based on paths:
   - `/app1` should route to App1
   - `/app2` should route to App2
5. Add TLS termination using a self-signed certificate

## Assignment 4: Horizontal Pod Autoscaler

Create a deployment with HPA configured:
1. Deploy a CPU-intensive application (you can use a custom image or find one that can generate load)
2. Configure resource requests and limits
3. Set up a Horizontal Pod Autoscaler that:
   - Scales based on CPU utilization (target 50%)
   - Has a minimum of 1 pod
   - Has a maximum of 10 pods
   - Has appropriate scaling behavior (cooldown periods, etc.)
4. Generate load on the application and observe the autoscaling behavior
5. Document the scaling events and metrics

## Assignment 5: Jobs and CronJobs

Implement a backup solution using Jobs and CronJobs:
1. Create a one-time Job that performs an initial backup of data
   - The job should use a custom script or command to backup data to a PersistentVolume
   - Configure the job with appropriate completions and parallelism
2. Create a CronJob that runs daily to perform incremental backups
   - Schedule it to run at a specific time
   - Configure history limits and concurrency policy
3. Create another CronJob that runs weekly to perform full backups
4. Test the backup and restore process

## Submission Guidelines

- Create YAML files for all resources
- Include a README.md with explanations of your solutions and any commands used
- Document any challenges faced and how you resolved them
- Include screenshots or logs demonstrating the functionality
