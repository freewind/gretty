/*
 * Copyright 2009-2011 MBTE Sweden AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mbte.gretty.httpserver

import org.jboss.netty.channel.SimpleChannelHandler
import org.jboss.netty.channel.ChannelHandlerContext
import org.jboss.netty.channel.WriteCompletionEvent
import org.jboss.netty.channel.MessageEvent
import org.jboss.netty.buffer.ChannelBuffer

@Typed class IoMonitor extends SimpleChannelHandler {

    volatile long bytesSent, bytesReceived
    volatile long totalBytesSent, totalBytesReceived

    void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) {
        bytesSent.addAndGet(e.writtenAmount)
        totalBytesSent.addAndGet(e.writtenAmount)
        super.writeComplete(ctx, e)
    }

    void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        bytesReceived.addAndGet(((ChannelBuffer)e.message).readableBytes())
        totalBytesReceived.addAndGet(((ChannelBuffer)e.message).readableBytes())
        super.messageReceived(ctx, e)
    }

    long getAndCleanSent () {
        bytesSent.getAndSet(0)
    }

    long getAndCleanReceived () {
        bytesReceived.getAndSet(0)
    }
}
