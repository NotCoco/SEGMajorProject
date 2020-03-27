<template>
  <div id="rich-text-editor">
    <editor-menu-bar v-if="editable" :editor="editor" v-slot="{ commands, isActive }">
      <div class="rich-text-editor-menu">
        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.bold() }"
          @click="commands.bold"
        >
          <i class="material-icons">format_bold</i>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.italic() }"
          @click="commands.italic"
        >
          <i class="material-icons">format_italic</i>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.underline() }"
          @click="commands.underline"
        >
          <i class="material-icons">format_underline</i>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.paragraph() }"
          @click="commands.paragraph"
        >
          <span class="rte-menu-button-text-icon">P</span>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.heading({ level: 1 }) }"
          @click="commands.heading({ level: 1 })"
        >
          <span class="rte-menu-button-text-icon">H1</span>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.heading({ level: 2 }) }"
          @click="commands.heading({ level: 2 })"
        >
          <span class="rte-menu-button-text-icon">H2</span>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.heading({ level: 3 }) }"
          @click="commands.heading({ level: 3 })"
        >
          <span class="rte-menu-button-text-icon">H3</span>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.bullet_list() }"
          @click="commands.bullet_list"
        >
          <i class="material-icons">format_list_bulleted</i>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.ordered_list() }"
          @click="commands.ordered_list"
        >
          <i class="material-icons">format_list_numbered</i>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.blockquote() }"
          @click="commands.blockquote"
        >
          <i class="material-icons">format_quote</i>
        </button>

        <button
          class="button is-light rte-menu-button"
          :class="{ 'is-active': isActive.notification() }"
          @click="commands.notification"
        >
          <i class="material-icons">note</i>
        </button>

        <button class="button is-light rte-menu-button" @click="commands.horizontal_rule">
          <i class="material-icons">remove</i>
        </button>

        <button class="button is-light rte-menu-button" @click="commands.undo">
          <i class="material-icons">undo</i>
        </button>

        <button class="button is-light rte-menu-button" @click="commands.redo">
          <i class="material-icons">redo</i>
        </button>
      </div>
    </editor-menu-bar>
    <editor-content class="content fullheight-editor" :editor="editor" />
  </div>
</template>
<style lang="scss" scoped>
#rich-text-editor {
  height: 100%;
}

.rich-text-editor-menu {
  margin-bottom: 10px;
}
</style>

<script>
import { Editor, EditorContent, EditorMenuBar } from "tiptap";
import {
  Placeholder,
  Bold,
  Italic,
  Underline,
  Heading,
  ListItem,
  BulletList,
  OrderedList,
  Blockquote,
  HorizontalRule,
  History,
  TrailingNode
} from "tiptap-extensions";

import Notification from "../components/Notification.js";

export default {
  components: {
    EditorMenuBar,
    EditorContent
  },
  props: {
    editable: {
      type: Boolean,
      default: true
    },
    value: {}
  },
  data() {
    return {
      editor: new Editor({
        extensions: [
          new Placeholder({
            emptyEditorClass: "is-editor-empty",
            emptyNodeClass: "is-empty",
            emptyNodeText: "Start typing page content here ...",
            showOnlyWhenEditable: true,
            showOnlyCurrent: true
          }),
          new Bold(),
          new Italic(),
          new Underline(),
          new Heading(),
          new BulletList(),
          new ListItem(),
          new OrderedList(),
          new Blockquote(),
          new HorizontalRule(),
          new History(),
          new TrailingNode({
            node: "paragraph",
            notAfter: ["paragraph"]
          }),
          new Notification()
        ],
        editable: this.editable,
        onUpdate: ({getJSON}) => {
          this.$emit("input", JSON.stringify(getJSON()));
        },
        content: this.value
      })
    };
  },
  watch: {
    value: function(val) {
      if (val !== JSON.stringify(this.editor.getJSON())) {
        this.editor.setContent(JSON.parse(val));
      }
    }
  },
  beforeDestroy() {
    setTimeout(() => {
      this.editor.destroy();
    }, 200);
  }
};
</script>