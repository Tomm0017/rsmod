package gg.rsmod.cache.ext

import gg.rsmod.cache.CacheFile
import gg.rsmod.cache.Filestore
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

fun ByteArray.readChunks(dataFile: CacheFile, offset: Int, size: Int, headerLength: Int): ByteBuf {
    val output = Unpooled.buffer(size)
    var currOffset = offset

    var bytesRead = 0
    while (bytesRead < size) {
        dataFile.seek((currOffset * Filestore.DATA_FILE_CHUNK_SIZE).toLong())

        var blockLength = size - bytesRead
        if (blockLength > Filestore.DATA_FILE_CHUNK_SIZE - headerLength) {
            blockLength = Filestore.DATA_FILE_CHUNK_SIZE - headerLength
        }

        val read = dataFile.read(this, 0, headerLength + blockLength)

        check(read == headerLength + blockLength)

        val nextOffset = ((this[4].toInt() and 0xFF) shl 16) or ((this[5].toInt() and 0xFF) shl 8) or ((this[6].toInt() and 0xFF))

        output.writeBytes(this, headerLength, blockLength)

        currOffset = nextOffset
        bytesRead += blockLength
    }
    return output
}