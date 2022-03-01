package hello;
//
//import io.netty.channel.*;
//
//import java.net.InetAddress;
//import java.util.Date;
//import java.util.Locale;
//
//@ChannelHandler.Sharable
//public class ServerHandler extends SimpleChannelInboundHandler<String> {
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
//        ctx.write("It is " + new Date() + " now.\r\n");
//        ctx.flush();
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String req) throws Exception {
//        String res;
//        boolean close = false;
//        if (req.isEmpty()){
//            res = "Please type somthing.\r\n";
//        } else if ("bye".equals(req.toLowerCase())){
//            res = "Have a good day!\r\n";
//            close = true;
//        }else{
//            res = "Did you say '" + req + "'?\r\n";
//        }
//
//        ChannelFuture f = ctx.write(res);
//
//        if (close){
//            f.addListener(ChannelFutureListener.CLOSE);
//        }
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.add(ctx.channel());
        for (Channel channel : channels){
            channel.writeAndFlush("[SERVER]" + incoming.remoteAddress() + " has joined!\n");
        }
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels){
            channel.write("[SERVER]" + incoming.remoteAddress() + " has left!\n");
        }
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = ctx.channel();

        for (Channel c: channels) {
            if (c != ctx.channel()) {
                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
            } else {
                c.writeAndFlush("[you] " + msg + '\n');
            }
        }
    }
}
