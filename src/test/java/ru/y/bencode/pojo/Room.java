package ru.y.bencode.pojo;

import ru.y.bencode.BencodeSerializable;

@BencodeSerializable
public class Room {
    private Integer room;

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Room{" +
                "room=" + room +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room1 = (Room) o;

        if (room != null ? !room.equals(room1.room) : room1.room != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return room != null ? room.hashCode() : 0;
    }
}
