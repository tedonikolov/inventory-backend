package bg.tuvarna.services.converters;

public interface Converter<T, Y> {

  T convertToDto(final Y entity);

  Y convertToEntity(final T dto);

  Y updateEntity(Y entity,final T dto);
}
