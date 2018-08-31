#!/usr/bin/env bash
echo "Downloading Paper for minecraft version $1"
git clone https://github.com/PaperMC/Paper.git
pushd $HOME/Paper
git config --global user.email "jenkins@user.com"
git config --global user.name "jenkins"
echo "Building Paper for minecraft version $1 (this might take a while)"
paper jar
popd
