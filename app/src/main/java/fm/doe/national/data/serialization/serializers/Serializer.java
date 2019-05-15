package fm.doe.national.data.serialization.serializers;

public interface Serializer<T> {
    String serialize(T data);
}
