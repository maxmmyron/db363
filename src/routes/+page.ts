import type { PageLoad } from "./$types"

export const load: PageLoad = async ({ params, fetch }) => {
  let res = null
  try {
    res = await fetch("http://localhost:5133/api/trains/all");
  } catch (err) {
    return {
      trains: [],
      err,
    };
  }

  const trains = await res.json();

  return {
    trains,
    err: null,
  };
}
