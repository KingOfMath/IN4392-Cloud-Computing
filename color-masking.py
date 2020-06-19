from PIL import Image
import requests
import io
import numpy as np

def resize_image(image_path, resized_path):
    print('into resizing  function')
    with Image.open(image_path) as image:
        image=imgtransfer(image)
        print('Image path open as image')
        image.thumbnail(tuple(x / 2 for x in image.size))
        image.save(resized_path)
        print('resized_path',resized_path)


def imgtransfer(image):
    imgo = np.array(image)
    imgo.setflags(write=1)
    img_copy = imgo.copy()
    mask = img_copy < 87
    img_copy[mask]=255
    img = Image.fromarray(img_copy)
    return img

image_url='https://i0.wp.com/www.thebigraise.fr/wp-content/uploads/2019/11/Blue-Whales-1.jpg?resize=1280%2C640&ssl=1'
image_path='whale1.jpg'

with open(image_path, 'wb') as handle:
    response = requests.get(image_url, stream=True)
    if not response.ok:
        print(response)
    for block in response.iter_content(1024):
        if not block:
            break
        handle.write(block)

resized_path='whale1-color.jpg'
resize_image(image_path, resized_path)
