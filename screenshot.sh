#!/bin/bash

only_frameit=false

function show_help {
  echo "-f - create only framed screenshot images"
}
function read_arguments {
  while getopts "fh" opt; do
    case "$opt" in
      f)
        only_frameit=true
        ;;
      h)
         show_help
         exit
         ;;
      *)
        echo "invalid argument"
        exit
    esac
  done
}

function main {
  read_arguments
  mv fastlane/metadata/android/*/$(ls -I '**') /target/dir
}


main




