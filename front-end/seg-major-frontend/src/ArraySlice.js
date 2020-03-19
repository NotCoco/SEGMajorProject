export default class ArraySlice {
  #array;
  #from;
  length;

  constructor(array, from, length) {
    if (!(array instanceof Array)) throw new Error("array must be array");
    if (length < 0 || length > array.length - from) throw new Error("length out of bounds");
    if (length !== 0 && (from < 0 || from >= array.length)) throw new Error("from out of bounds");
    this.#array = array;
    this.#from = from;
    this.length = length;
  }

  get(i) {
    if (i < 0 || i >= length) throw new Error("Index out of bounds");
    return this.#array[i + this.#from];
  }

  [Symbol.iterator]() {
    console.log(this);
    var index = this.#from - 1;

    return {
      next: () => ({
        value: this.#array[++index],
        done: index >= this.length
      })
    };
  }
}
