import boto3
import os
import sys
import uuid
from urllib.parse import unquote_plus
from PIL import Image
import PIL.Image
import numpy as np

s3_client = boto3.client('s3')

def imgtransfer(image):
    imgo = np.array(image)
    img_copy = imgo.copy()
    imgo.setflags(write=1)
    mask = img_copy < 87
    img_copy[mask]=255
    img = Image.fromarray(img_copy)
    #img.save('my.png')
    return img

def resize_image(image_path, resized_path):
    print('into resizing  function')
    with Image.open(image_path) as image:
        print('Image path open as image')
        image = imgtransfer(image)
        image.thumbnail(tuple(x / 2 for x in image.size))
        image.save(resized_path)
        print('resized_path',resized_path)

def lambda_handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = unquote_plus(record['s3']['object']['key'])
        tmpkey = key.replace('/', '')
        download_path = '/tmp/{}{}'.format(uuid.uuid4(), tmpkey)
        upload_path = '/tmp/resized-{}'.format(tmpkey)
        s3_client.download_file(bucket, key, download_path)
        print('get object image, now resizing')
        resize_image(download_path, upload_path)
        print('upload_path',upload_path)
        s3_client.upload_file(upload_path, '{}-resized'.format(bucket), key)