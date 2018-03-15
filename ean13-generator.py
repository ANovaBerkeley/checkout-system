#! /usr/bin/python

"""
This script generates a random EAN13 number and prints it to the standard out.
"""

from random import randrange
import csv

def barcode_students():
    numbers = []
    numbers.append(978020)
    numbers.append(13)
    for _ in range(4):
        numbers.append(randrange(10))
    return numbers

barcodes = []
while len(barcodes) < 75:
    numbers = barcode_students()
    if numbers not in barcodes:
        print ''.join(map(str, numbers))
        barcodes.append(numbers)

