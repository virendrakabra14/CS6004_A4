#!/bin/bash

total=0

while IFS= read -r line; do
    num=$(echo "$line" | grep -o 'out of [0-9]*' | grep -o '[0-9]*')
    total=$(($total + $num))
done

echo "Total count: $total"
