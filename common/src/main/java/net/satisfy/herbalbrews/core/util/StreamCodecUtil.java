package net.satisfy.herbalbrews.core.util;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class StreamCodecUtil {
    public static <T, B extends FriendlyByteBuf> StreamCodec<B, NonNullList<T>> nonNullList(StreamCodec<B, T> elementCodec, T defaultElement) {
        return StreamCodec.of((buf, value) -> {
            buf.writeVarInt(value.size());

            for (T element : value) {
                elementCodec.encode(buf, element);
            }
        }, buf -> {
            NonNullList<T> list = NonNullList.withSize(buf.readVarInt(), defaultElement);

            list.replaceAll(element -> elementCodec.decode(buf));

            return list;
        });
    }
}
