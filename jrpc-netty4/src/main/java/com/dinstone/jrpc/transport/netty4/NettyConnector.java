/*
 * Copyright (C) 2012~2014 dinstone<dinstone@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dinstone.jrpc.transport.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dinstone.jrpc.transport.TransportConfig;

public class NettyConnector {

    private static final Logger LOG = LoggerFactory.getLogger(NettyConnector.class);

    private int refCount;

    private NioEventLoopGroup workerGroup;

    private Bootstrap boot;

    public NettyConnector(InetSocketAddress isa, TransportConfig config) {
        workerGroup = new NioEventLoopGroup();
        boot = new Bootstrap().group(workerGroup).channel(NioSocketChannel.class);
        boot.option(ChannelOption.SO_KEEPALIVE, true);
        boot.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new TransportProtocolDecoder());
                ch.pipeline().addLast(new TransportProtocolEncoder());
                ch.pipeline().addLast(new NettyClientHandler());
            }
        });

        boot.remoteAddress(isa);
    }

    /**
    *
    */
    public void incrementRefCount() {
        ++refCount;
    }

    /**
    *
    */
    public void decrementRefCount() {
        if (refCount > 0) {
            --refCount;
        }
    }

    /**
     * @return
     */
    public boolean isZeroRefCount() {
        return refCount == 0;
    }

    public void dispose() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    public Channel createSession() {
        ChannelFuture cf = boot.connect().awaitUninterruptibly();
        Channel channel = cf.channel();
        LOG.debug("session connect {} to {}", channel.localAddress(), channel.remoteAddress());
        return channel;
    }
}
