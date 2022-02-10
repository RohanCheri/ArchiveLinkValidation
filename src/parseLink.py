import requests
from bs4 import BeautifulSoup
# from urllib.parse import urlparse

base_url = "https://wayback.archive-it.org/5591/20210726190321/http://mcqmc2016.stanford.edu"

def validate_url(url):
    import re
    regex = re.compile(
        r'^https?://'  # http:// or https://
        r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+[A-Z]{2,6}\.?|'  # domain...
        r'localhost|'  # localhost...
        r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
        r'(?::\d+)?'  # optional port
        r'(?:/?|[/?]\S+)$', re.IGNORECASE)
    return url is not None and regex.search(url)

def get_links(base_url, db, level=0):
    page = requests.get(base_url)
    if(level > 1 or base_url in db):
        return []
    db[base_url] = page.status_code

    soup = BeautifulSoup(page.content, 'html.parser')

    links = [tag.get('href') for tag in soup.find_all('a')]
    print(base_url)

    for link in links:
        if(validate_url(link) ):
            get_links(link, db, level+1)
        else:
            if(link):
                get_links(base_url+link, db, level+1)


    return links


db = dict()
level = 0
links = get_links(base_url, db, level)

# for link in links:
#     if(validate_url(link) ):
#         get_links(link, db, level+1)
#     else:
#         get_links(base_url+link, db, level+1)


for link in db.keys():
    print(link)
    print(db[link])

