package fm.doe.national.data.serializers;

public interface Serializer<T> {
    String serialize(T data);
}
