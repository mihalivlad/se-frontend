// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
      --lumo-font-family: Consolas, "Andale Mono WT", "Andale Mono", "Lucida Console", "Lucida Sans Typewriter", "DejaVu Sans Mono", "Bitstream Vera Sans Mono", "Liberation Mono", "Nimbus Mono L", Monaco, "Courier New", Courier, monospace;
      --lumo-border-radius: calc(var(--lumo-size-m) / 2);
      --lumo-size-xl: 4rem;
      --lumo-size-l: 3rem;
      --lumo-size-m: 2.5rem;
      --lumo-size-s: 2rem;
      --lumo-size-xs: 1.75rem;
      --lumo-space-xl: 2.5rem;
      --lumo-space-l: 1.75rem;
      --lumo-space-m: 1.125rem;
      --lumo-space-s: 0.75rem;
      --lumo-space-xs: 0.375rem;
      --lumo-shade-5pct: rgba(33, 33, 33, 0.05);
      --lumo-shade-10pct: rgba(33, 33, 33, 0.1);
      --lumo-shade-20pct: rgba(33, 33, 33, 0.2);
      --lumo-shade-30pct: rgba(33, 33, 33, 0.3);
      --lumo-shade-40pct: rgba(33, 33, 33, 0.4);
      --lumo-shade-50pct: rgba(33, 33, 33, 0.5);
      --lumo-shade-60pct: rgba(33, 33, 33, 0.6);
      --lumo-shade-70pct: rgba(33, 33, 33, 0.7);
      --lumo-shade-80pct: rgba(33, 33, 33, 0.8);
      --lumo-shade-90pct: rgba(33, 33, 33, 0.9);
      --lumo-primary-color-50pct: rgba(235, 89, 5, 0.5);
      --lumo-primary-color-10pct: rgba(235, 89, 5, 0.1);
      --lumo-error-color-50pct: rgba(231, 24, 24, 0.5);
      --lumo-error-color-10pct: rgba(231, 24, 24, 0.1);
      --lumo-success-color-50pct: rgba(62, 229, 170, 0.5);
      --lumo-success-color-10pct: rgba(62, 229, 170, 0.1);
      --lumo-shade: hsl(0, 0%, 13%);
      --lumo-primary-color: hsl(22, 96%, 47%);
      --lumo-primary-text-color: hsl(22, 100%, 42%);
      --lumo-error-color: hsl(0, 81%, 50%);
      --lumo-error-text-color: hsl(0, 86%, 45%);
      --lumo-success-color: hsl(159, 76%, 57%);
      --lumo-success-contrast-color: hsl(159, 29%, 10%);
      --lumo-success-text-color: hsl(159, 61%, 40%);
    }

    [theme~="dark"] {
      --lumo-shade-5pct: rgba(33, 33, 33, 0.05);
      --lumo-shade-10pct: rgba(33, 33, 33, 0.1);
      --lumo-shade-20pct: rgba(33, 33, 33, 0.2);
      --lumo-shade-30pct: rgba(33, 33, 33, 0.3);
      --lumo-shade-40pct: rgba(33, 33, 33, 0.4);
      --lumo-shade-50pct: rgba(33, 33, 33, 0.5);
      --lumo-shade-60pct: rgba(33, 33, 33, 0.6);
      --lumo-shade-70pct: rgba(33, 33, 33, 0.7);
      --lumo-shade-80pct: rgba(33, 33, 33, 0.8);
      --lumo-shade-90pct: rgba(33, 33, 33, 0.9);
      --lumo-primary-color-50pct: rgba(235, 89, 5, 0.5);
      --lumo-primary-color-10pct: rgba(235, 89, 5, 0.1);
      --lumo-error-color-50pct: rgba(231, 24, 24, 0.5);
      --lumo-error-color-10pct: rgba(231, 24, 24, 0.1);
      --lumo-success-color-50pct: rgba(62, 229, 170, 0.5);
      --lumo-success-color-10pct: rgba(62, 229, 170, 0.1);
      --lumo-shade: hsl(0, 0%, 13%);
      --lumo-primary-color: hsl(22, 96%, 47%);
      --lumo-primary-text-color: hsl(22, 100%, 42%);
      --lumo-error-color: hsl(0, 81%, 50%);
      --lumo-error-text-color: hsl(0, 86%, 45%);
      --lumo-success-color: hsl(159, 76%, 57%);
      --lumo-success-contrast-color: hsl(159, 29%, 10%);
      --lumo-success-text-color: hsl(159, 61%, 40%);
    }
  </style>
</custom-style>

<dom-module id="text-field-style" theme-for="vaadin-text-field vaadin-text-area vaadin-password-field">
  <template>
    <style>
      [part="input-field"] {
        box-shadow: inset 0 0 0 1px var(--lumo-contrast-30pct);
        background-color: var(--lumo-base-color);
      }

      :host([invalid]) [part="input-field"] {
        box-shadow: inset 0 0 0 1px var(--lumo-error-color);
      }
    </style>
  </template>
</dom-module>

`;

document.head.appendChild($_documentContainer.content);
