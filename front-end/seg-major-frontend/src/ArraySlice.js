export default class ArraySlice {
  #array;
  #from;
  length;

  constructor(array, from, maxLength) {
    if (!(array instanceof Array)) throw new Error("array must be array");
    if (maxLength < 0) throw new Error("maxLength cannot be negative");
    this.#array = array;
    if (array.length === 0) {
      this.#from = -1;
      this.length = 0;
    } else {
      if (from < 0 || from >= array.length) throw new Error("from out of bounds");
      this.#from = from;
      this.length = Math.min(maxLength, array.length - from);
    }
  }

  get(i) {
    if (i < 0 || i >= this.length) throw new Error("Index out of bounds");
    return this.#array[i + this.#from];
  }

  [Symbol.iterator]() {
    let index = this.#from - 1;
    const lastIndex = index + this.length;

    return {
      next: () => ({
        value: this.#array[++index],
        done: index > lastIndex
      })
    };
  }
}
