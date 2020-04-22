package com.lagou.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readInt();
        ByteBuf byteBuf = in.readBytes(length);
        byte[] bytes;
        if (byteBuf.hasArray()) {
            bytes = byteBuf.array();
        } else {
            bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        }
//        byteBuf.readBytes(bytes);
        Object o = serializer.deserialize(clazz, bytes);
        out.add(o);
    }
}
