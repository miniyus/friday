#!/bin/bash

# feature: git pull
# Args: git-pull.sh $1={branch name}

branch_name=$1
env=$2
repo_name="origin"

echo "[git pull]"
echo "option: branch_name=$branch_name"
echo "option: env=$env"

if ! git --version > /dev/null 2>&1; then
  echo "git is not installed"
  exit 1
fi

echo "git fetch --all"
git fetch --all

if [ "$branch_name" != "" ];then
  echo "git checkout $branch_name"
  git checkout $branch_name
fi

git pull $repo_name $branch_name

git secret reveal -f .env.$env