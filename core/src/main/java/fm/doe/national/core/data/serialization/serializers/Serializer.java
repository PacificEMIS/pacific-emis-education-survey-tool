package fm.doe.national.core.data.serialization.serializers;

public interface Serializer<T> {
    String serialize(T data);
}
