/**
 * Copyright (c) 2010-2011 Graham Edgecombe
 * Copyright (c) 2011-2016 Major <major.emrs@gmail.com> and other apollo contributors
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package gg.rsmod.net.packet

/**
 * Represents the different simple data types.
 *
 * @param bytes The number of bytes this type occupies.
 *
 * @author Graham
 */
enum class DataType(val bytes: Int) {

    /**
     * A byte.
     */
    BYTE(1),

    /**
     * A short.
     */
    SHORT(2),

    /**
     * A 'tri byte' - a group of three bytes.
     */
    TRI_BYTE(3),

    /**
     * An integer.
     */
    INT(4),

    /**
     * A long.
     */
    LONG(8),

    /**
     * A byte array.
     */
    BYTES(-1),

    SMART(-1),

    /**
     * A String.
     */
    STRING(-1),

}