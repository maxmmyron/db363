/**
 * Returns a list of Links.
 * @returns
 */
export async function GET({ fetch }) {
  return await fetch("http://localhost:5133/api/links");
}
