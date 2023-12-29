#!/bin/bash

echo "[Clean up images] '<none>'"
docker image rm $(docker image list -f 'dangling=true' -q --no-trunc)
echo ""

echo "[Clean up builder] until 24h"
echo "y" | docker builder prune --filter until=24h
echo ""