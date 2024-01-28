FROM python:3-alpine
WORKDIR /home/app
COPY main.py /home/app/
COPY requirements.txt /home/app/

RUN pip install --trusted-host mirrors.aliyun.com --index-url http://mirrors.aliyun.com/pypi/simple/ --requirement requirements.txt --no-cache-dir

VOLUME ["/home/app/data/"]
CMD ["python3","-B","main.py"]