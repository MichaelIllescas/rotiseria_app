import * as React from "react";
import * as MenubarPrimitive from "@radix-ui/react-menubar";
import { CheckIcon, ChevronRightIcon, CircleIcon } from "lucide-react";
import { cn } from "./utils";

/**
 * 📌 Menubar
 *
 * Barra de menú horizontal basada en Radix UI.
 * Sirve para agrupar menús desplegables con items, accesos rápidos
 * y submenús. Muy común en aplicaciones tipo escritorio (File, Edit, View...).
 *
 * ✅ Props principales:
 * - className: (string) → clases extra para personalizar estilos.
 * - ...props: cualquier prop aceptada por `MenubarPrimitive.Root`.
 *
 * ⚠️ Si no se pasan props:
 * - Renderiza la barra vacía, lista para recibir menús.
 */
function Menubar({ className, ...props }) {
  return (
    <MenubarPrimitive.Root
      data-slot="menubar"
      className={cn(
        "bg-background flex h-9 items-center gap-1 rounded-md border p-1 shadow-xs",
        className
      )}
      {...props}
    />
  );
}

/** 📌 MenubarMenu */
function MenubarMenu(props) {
  return <MenubarPrimitive.Menu data-slot="menubar-menu" {...props} />;
}

/** 📌 MenubarGroup */
function MenubarGroup(props) {
  return <MenubarPrimitive.Group data-slot="menubar-group" {...props} />;
}

/** 📌 MenubarPortal */
function MenubarPortal(props) {
  return <MenubarPrimitive.Portal data-slot="menubar-portal" {...props} />;
}

/** 📌 MenubarRadioGroup */
function MenubarRadioGroup(props) {
  return (
    <MenubarPrimitive.RadioGroup data-slot="menubar-radio-group" {...props} />
  );
}

/**
 * 📌 MenubarTrigger
 *
 * Botón que abre el menú asociado.
 */
function MenubarTrigger({ className, ...props }) {
  return (
    <MenubarPrimitive.Trigger
      data-slot="menubar-trigger"
      className={cn(
        "focus:bg-accent focus:text-accent-foreground data-[state=open]:bg-accent data-[state=open]:text-accent-foreground flex items-center rounded-sm px-2 py-1 text-sm font-medium outline-hidden select-none",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 MenubarContent
 *
 * Contenedor desplegable del menú.
 */
function MenubarContent({
  className,
  align = "start",
  alignOffset = -4,
  sideOffset = 8,
  ...props
}) {
  return (
    <MenubarPortal>
      <MenubarPrimitive.Content
        data-slot="menubar-content"
        align={align}
        alignOffset={alignOffset}
        sideOffset={sideOffset}
        className={cn(
          "bg-popover text-popover-foreground data-[state=open]:animate-in data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 z-50 min-w-[12rem] rounded-md border p-1 shadow-md",
          className
        )}
        {...props}
      />
    </MenubarPortal>
  );
}

/**
 * 📌 MenubarItem
 *
 * Item genérico dentro de un menú.
 */
function MenubarItem({ className, inset, variant = "default", ...props }) {
  return (
    <MenubarPrimitive.Item
      data-slot="menubar-item"
      data-inset={inset}
      data-variant={variant}
      className={cn(
        "focus:bg-accent focus:text-accent-foreground data-[variant=destructive]:text-destructive relative flex cursor-default items-center gap-2 rounded-sm px-2 py-1.5 text-sm select-none",
        "data-[disabled]:pointer-events-none data-[disabled]:opacity-50 data-[inset]:pl-8",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 MenubarCheckboxItem
 *
 * Item con checkbox dentro de un menú.
 */
function MenubarCheckboxItem({ className, children, checked, ...props }) {
  return (
    <MenubarPrimitive.CheckboxItem
      data-slot="menubar-checkbox-item"
      className={cn(
        "focus:bg-accent focus:text-accent-foreground relative flex cursor-default items-center gap-2 rounded-xs py-1.5 pr-2 pl-8 text-sm select-none data-[disabled]:opacity-50",
        className
      )}
      checked={checked}
      {...props}
    >
      <span className="absolute left-2 flex size-3.5 items-center justify-center">
        <MenubarPrimitive.ItemIndicator>
          <CheckIcon className="size-4" />
        </MenubarPrimitive.ItemIndicator>
      </span>
      {children}
    </MenubarPrimitive.CheckboxItem>
  );
}

/**
 * 📌 MenubarRadioItem
 *
 * Item de tipo radio dentro de un `MenubarRadioGroup`.
 */
function MenubarRadioItem({ className, children, ...props }) {
  return (
    <MenubarPrimitive.RadioItem
      data-slot="menubar-radio-item"
      className={cn(
        "focus:bg-accent focus:text-accent-foreground relative flex cursor-default items-center gap-2 rounded-xs py-1.5 pr-2 pl-8 text-sm select-none data-[disabled]:opacity-50",
        className
      )}
      {...props}
    >
      <span className="absolute left-2 flex size-3.5 items-center justify-center">
        <MenubarPrimitive.ItemIndicator>
          <CircleIcon className="size-2 fill-current" />
        </MenubarPrimitive.ItemIndicator>
      </span>
      {children}
    </MenubarPrimitive.RadioItem>
  );
}

/** 📌 MenubarLabel */
function MenubarLabel({ className, inset, ...props }) {
  return (
    <MenubarPrimitive.Label
      data-slot="menubar-label"
      data-inset={inset}
      className={cn("px-2 py-1.5 text-sm font-medium data-[inset]:pl-8", className)}
      {...props}
    />
  );
}

/** 📌 MenubarSeparator */
function MenubarSeparator({ className, ...props }) {
  return (
    <MenubarPrimitive.Separator
      data-slot="menubar-separator"
      className={cn("bg-border -mx-1 my-1 h-px", className)}
      {...props}
    />
  );
}

/** 📌 MenubarShortcut */
function MenubarShortcut({ className, ...props }) {
  return (
    <span
      data-slot="menubar-shortcut"
      className={cn("text-muted-foreground ml-auto text-xs tracking-widest", className)}
      {...props}
    />
  );
}

/** 📌 MenubarSub */
function MenubarSub(props) {
  return <MenubarPrimitive.Sub data-slot="menubar-sub" {...props} />;
}

/** 📌 MenubarSubTrigger */
function MenubarSubTrigger({ className, inset, children, ...props }) {
  return (
    <MenubarPrimitive.SubTrigger
      data-slot="menubar-sub-trigger"
      data-inset={inset}
      className={cn(
        "focus:bg-accent focus:text-accent-foreground flex cursor-default items-center rounded-sm px-2 py-1.5 text-sm select-none",
        className
      )}
      {...props}
    >
      {children}
      <ChevronRightIcon className="ml-auto h-4 w-4" />
    </MenubarPrimitive.SubTrigger>
  );
}

/** 📌 MenubarSubContent */
function MenubarSubContent({ className, ...props }) {
  return (
    <MenubarPrimitive.SubContent
      data-slot="menubar-sub-content"
      className={cn(
        "bg-popover text-popover-foreground data-[state=open]:animate-in data-[state=closed]:animate-out z-50 min-w-[8rem] rounded-md border p-1 shadow-lg",
        className
      )}
      {...props}
    />
  );
}

/* 🔹 Exportación */
export {
  Menubar,
  MenubarPortal,
  MenubarMenu,
  MenubarTrigger,
  MenubarContent,
  MenubarGroup,
  MenubarSeparator,
  MenubarLabel,
  MenubarItem,
  MenubarShortcut,
  MenubarCheckboxItem,
  MenubarRadioGroup,
  MenubarRadioItem,
  MenubarSub,
  MenubarSubTrigger,
  MenubarSubContent,
};
