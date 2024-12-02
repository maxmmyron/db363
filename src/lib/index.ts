export const formatAPIObject = (endpoint: string, obj: Object) => {
  if(obj == null) return endpoint;
  if(endpoint.endsWith("/")) endpoint = endpoint.substring(0, endpoint.length - 1) + "?";
  else endpoint = endpoint + "?";
  Object.entries(obj).forEach(([key, val]) => endpoint += `${key}=${val}&`);
  return endpoint.substring(0, endpoint.length - 1);
}
