package github.zjm404.zrpc.protocol;

import github.zjm404.zrpc.core.ISerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ProtocolVersionOne {
    public static final short MAGIC = 0x0223;
    public static final byte VERSION = 1;
    public static final byte HEADER_SIZE_WITHOUT_EXTENSION = 19;
}
