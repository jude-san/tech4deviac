#!/bin/bash
set -e

echo "Installing MetalLB..."

# Apply MetalLB manifests
kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/v0.14.9/config/manifests/metallb-native.yaml

# Wait for MetalLB to be ready
echo "Waiting for MetalLB pods to be ready..."
kubectl wait --namespace metallb-system \
  --for=condition=ready pod \
  --selector=app=metallb \
  --timeout=90s

# Get the Docker network subnet
DOCKER_SUBNET=$(docker network inspect kind | jq -r '.[0].IPAM.Config[0].Subnet')
IP_PREFIX=$(echo $DOCKER_SUBNET | sed 's/\.[0-9]*\.[0-9]*\/.*$//')

# Create MetalLB IP address pool
cat <<EOF | kubectl apply -f -
apiVersion: metallb.io/v1beta1
kind: IPAddressPool
metadata:
  name: default
  namespace: metallb-system
spec:
  addresses:
  - ${IP_PREFIX}.117.200-${IP_PREFIX}.117.250
EOF

# Create L2Advertisement
cat <<EOF | kubectl apply -f -
apiVersion: metallb.io/v1beta1
kind: L2Advertisement
metadata:
  name: default
  namespace: metallb-system
spec:
  ipAddressPools:
  - default
EOF

echo "MetalLB has been installed and configured!"
echo "IP address pool: ${IP_PREFIX}.117.200-${IP_PREFIX}.117.250"
