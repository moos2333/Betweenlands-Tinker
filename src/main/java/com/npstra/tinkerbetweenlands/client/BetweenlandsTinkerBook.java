package com.npstra.tinkerbetweenlands.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.BookLoader;
import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.data.content.ContentTableOfContents;
import slimeknights.mantle.client.book.data.content.PageContent;
import slimeknights.mantle.client.book.repository.ModuleFileRepository;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.book.content.ContentModifier;
import slimeknights.tconstruct.library.book.content.ContentModifierFortify;
import slimeknights.tconstruct.library.book.content.ContentTool;
import slimeknights.tconstruct.library.book.sectiontransformer.BowMaterialSectionTransformer;
import slimeknights.tconstruct.library.book.sectiontransformer.ModifierSectionTransformer;
import slimeknights.tconstruct.library.book.sectiontransformer.ToolSectionTransformer;

import java.util.Iterator;

@SideOnly(Side.CLIENT)
public class BetweenlandsTinkerBook {
    private static BookData instance;

    public static BookData get() {
        if (instance == null) {
            instance = BookLoader.registerBook(
                    "tinkerbetweenlands:betweenlands_tinker_book",
                    new ModuleFileRepository(TConstruct.pulseManager, "tinkerbetweenlands:book"));
            tryRegister("tool", ContentTool.class);
            tryRegister("modifier", ContentModifier.class);
            tryRegister("modifier_fortify", ContentModifierFortify.class);
            instance.addTransformer(new BookFixTransformer());
            instance.addTransformer(new ToolSectionTransformer());
            instance.addTransformer(new ModifierSectionTransformer());
            instance.addTransformer(new BowMaterialSectionTransformer());
            instance.addTransformer(BookTransformer.IndexTranformer());
        }
        return instance;
    }

    public static void init() {
        get();
    }

    private static void tryRegister(String name, Class<? extends PageContent> clazz) {
        try {
            BookLoader.registerPageType(name, clazz);
        } catch (IllegalArgumentException ignored) {
        }
    }

    private static class BookFixTransformer extends BookTransformer {
        @Override
        public void transform(BookData book) {
            for (SectionData section : book.sections) {
                if (section.parent == null) {
                    section.parent = book;
                }
                Iterator<PageData> it = section.pages.iterator();
                while (it.hasNext()) {
                    PageData page = it.next();
                    if (page.content instanceof ContentTableOfContents) {
                        it.remove();
                        continue;
                    }
                    if (page.parent == null) {
                        page.parent = section;
                    }
                }
            }
        }
    }
}