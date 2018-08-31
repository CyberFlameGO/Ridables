#!/usr/bin/env bash
mkdir -p $HOME/build
pushd $HOME/build
echo "Downloading Paper..."
git clone https://github.com/PaperMC/Paper.git
pushd $HOME/build/Paper
git config --global --unset core.autocrlf
git config --global user.email "jenkins@notset.com"
git config --global user.name "jenkins"
git checkout pre/1.13
echo "Building Paper (this might take a while)..."
paper patch
paper jar
popd
