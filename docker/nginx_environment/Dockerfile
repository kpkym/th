FROM debian:buster-slim

RUN apt update && apt install -y build-essential wget


VOLUME ["/etc/fdfs", "/fastdfs/tracker/data", "/fastdfs/store_path", "/fastdfs/storage/data", "/fastdfs/client"]
COPY fdfs/* /etc/fdfs/

WORKDIR /opt

RUN wget https://github.com/happyfish100/libfastcommon/archive/V1.0.43.tar.gz \
    && tar -zxvf V1.0.43.tar.gz \
    && cd libfastcommon-1.0.43 \
    && ./make.sh clean && ./make.sh && ./make.sh install

RUN wget https://github.com/happyfish100/fastdfs/archive/V6.06.tar.gz \
    && tar -zxvf V6.06.tar.gz \
    && cd fastdfs-6.06 \
    && ./make.sh clean && ./make.sh && ./make.sh install

RUN wget https://ftp.pcre.org/pub/pcre/pcre-8.44.tar.gz \
    && tar -zxvf pcre-8.44.tar.gz \
    && cd pcre-8.44 \
    && ./configure \
    && make \
    && make install

RUN wget http://zlib.net/zlib-1.2.11.tar.gz \
    && tar -zxvf zlib-1.2.11.tar.gz \
    && cd zlib-1.2.11 \
    && ./configure \
    && make && make install

RUN wget http://www.openssl.org/source/openssl-1.1.1d.tar.gz \
    && tar -zxvf openssl-1.1.1d.tar.gz \
    && cd openssl-1.1.1d \
    && ./config \
    && make \
    && make install

RUN wget https://github.com/happyfish100/fastdfs-nginx-module/archive/V1.22.tar.gz \
    && tar -zxvf V1.22.tar.gz

RUN wget https://nginx.org/download/nginx-1.16.1.tar.gz \
    && tar -zxvf nginx-1.16.1.tar.gz

RUN cd nginx-1.16.1 && ./configure \
--sbin-path=/usr/local/nginx/nginx \
--conf-path=/usr/local/nginx/nginx.conf \
--pid-path=/usr/local/nginx/nginx.pid \
--with-pcre=../pcre-8.44 \
--with-zlib=../zlib-1.2.11 \
--with-http_ssl_module \
--with-stream \
--with-mail=dynamic \
--without-http_gzip_module \
--add-module=/opt/fastdfs-nginx-module-1.22/src \
&& make && make install

COPY nginx.conf /usr/local/nginx/nginx.conf
COPY startup.sh startup.sh

EXPOSE 80
CMD sh startup.sh
