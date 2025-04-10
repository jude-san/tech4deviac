# Kubernetes Basics: Take-Home Assignments

Complete the following assignments to reinforce your understanding of basic Kubernetes concepts.

## Assignment 1: Multi-Container Pod

Create a Pod with two containers:
- A main application container running `nginx`
- A sidecar container running `busybox` that periodically (every 10 seconds) writes the current date and time to a shared volume mounted at `/usr/share/nginx/html/date.html`

Requirements:
- Use a shared volume that both containers can access
- Configure the nginx container to serve the content from the shared volume
- Verify that you can access the date.html file through the nginx container

## Assignment 2: Rolling Update Strategy

Create a Deployment with the following specifications:
- Use the `nginx:latest` image
- Set up 3 replicas
- Configure a rolling update strategy that:
  - Updates only one pod at a time (maxUnavailable=1)
  - Can surge up to 1 additional pod during updates (maxSurge=1)
- Add appropriate readiness and liveness probes

Then, update the Deployment to use `nginx:1.20` and observe the rolling update process.

## Assignment 3: ConfigMap and Secret Management

Create an application that uses both ConfigMaps and Secrets:

1. Create a ConfigMap with the following data:
   - `DATABASE_HOST`: `mysql-service`
   - `DATABASE_PORT`: `3306`
   - `APP_COLOR`: `blue`
   - `app.properties`: A multi-line property file with at least 5 configuration settings

2. Create a Secret with the following data:
   - `DATABASE_USER`: `admin`
   - `DATABASE_PASSWORD`: `password123`

3. Create a Pod that uses both the ConfigMap and Secret:
   - Mount the `app.properties` file to `/etc/app/config/app.properties`
   - Expose the remaining ConfigMap entries as environment variables
   - Expose the Secret entries as environment variables
   - Use a container command that prints out all environment variables and the content of the config file

## Assignment 4: Resource Management

Create a namespace with resource quotas and limits:

1. Create a namespace called `limited-resources`
2. Apply a ResourceQuota to the namespace with the following constraints:
   - Max 5 pods
   - Max CPU request of 1 core total
   - Max Memory request of 1Gi total
   - Max CPU limit of 2 cores total
   - Max Memory limit of 2Gi total

3. Create a LimitRange that sets default requests and limits for containers in the namespace

4. Deploy multiple pods to test the resource quota enforcement

## Assignment 5: Service Discovery and Networking

Create a multi-tier application with proper service discovery:

1. Create a backend Deployment with 2 replicas running a simple API service
2. Create a ClusterIP Service for the backend
3. Create a frontend Deployment with 2 replicas that connects to the backend service
4. Create a LoadBalancer Service for the frontend
5. Demonstrate that the frontend can successfully communicate with the backend using the service name

## Submission Guidelines

- Create YAML files for all resources
- Include a README.md with explanations of your solutions and any commands used
- Document any challenges faced and how you resolved them
