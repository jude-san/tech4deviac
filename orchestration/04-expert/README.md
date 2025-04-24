## Lab Objectives

- Create and deploy Validation and Mutating Webhooks
- Install and configure Hashicorp Vault for secret management
- Set up External Secrets Operator to sync secrets from Vault to Kubernetes
- Implement advanced security policies with OPA Gatekeeper

## Prerequisites

- Completion of the advanced lab
- A running Kind cluster with MetalLB
- Cert-Manager installed and configured

## Exercises

### 1. Validation and Mutating Webhooks

Webhooks are HTTP callbacks that receive admission requests and do something with them.

```bash
# Create the webhook service and deployment
kubectl apply -f 01-webhook-service.yaml

# Create the webhook configuration
kubectl apply -f 01-webhook-configuration.yaml

# Test the validation webhook
kubectl apply -f 01-test-validation.yaml
# This should be rejected if it doesn't meet the validation criteria

# Test the mutating webhook
kubectl apply -f 01-test-mutation.yaml
# Check that the resource was modified by the webhook
kubectl get pod mutated-pod -o yaml

### 2. Hashicorp Vault Installation

Vault securely stores and controls access to tokens, passwords, certificates, and encryption keys.

```bash
# Install Vault
kubectl apply -f 02-vault.yaml

# Wait for Vault to be ready
kubectl wait --for=condition=ready pod -l app=vault --timeout=120s

# Initialize Vault
kubectl exec -it vault-0 -- vault operator init -n 1 -t 1
# Save the unseal key and root token

# Unseal Vault
kubectl exec -it vault-0 -- vault operator unseal <UNSEAL_KEY>

# Configure Vault
kubectl exec -it vault-0 -- /bin/sh
# Inside the pod, authenticate and enable the Kubernetes auth method
export VAULT_TOKEN=<ROOT_TOKEN>
vault auth enable kubernetes
vault write auth/kubernetes/config \
  kubernetes_host=https://kubernetes.default.svc.cluster.local:443

# Create a secrets engine and policy
vault secrets enable -path=secret kv-v2
vault policy write app-policy - <<EOF
path "secret/data/app/*" {
  capabilities = ["read"]
}
EOF

# Create a Kubernetes auth role
vault write auth/kubernetes/role/app \
  bound_service_account_names=app-sa \
  bound_service_account_namespaces=default \
  policies=app-policy \
  ttl=1h

# Create a sample secret
vault kv put secret/app/config username="app-user" password="supersecret"
```

### 3. External Secrets Operator

# Install External Secrets Operator (ESO)

```bash
helm repo add external-secrets https://charts.external-secrets.io
helm install external-secrets \
   external-secrets/external-secrets \
    -n external-secrets \
    --create-namespace

# Configure a SecretStore that connects to Vault
kubectl apply -f 03-external-secrets-store.yaml

# Create an ExternalSecret that fetches from Vault
kubectl apply -f 03-external-secret.yaml

# Verify that the Kubernetes Secret was created
kubectl get secret app-credentials
```

### 4. OPA Gatekeeper for Policy Enforcement

```bash
# Install OPA Gatekeeper
kubectl apply -f https://raw.githubusercontent.com/open-policy-agent/gatekeeper/v3.19.0/deploy/gatekeeper.yaml

# Create a ConstraintTemplate
kubectl apply -f 04-constraint-template.yaml

# Create a Constraint based on the template
kubectl apply -f 04-constraint.yaml

# Test the constraint - this should be rejected
kubectl apply -f 04-test-reject.yaml

# Test the constraint - this should be allowed
kubectl apply -f 04-test-allow.yaml
```
