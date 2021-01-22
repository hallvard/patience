package no.hal.patience.fx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.value.WritableValue;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.fx.PileView;

public class FxUtil {

    public static <P extends Enum<P>> List<Pile> getAllPiles(Patience<P> patience, Enum<P>... categoriesOrNames) {
        List<Pile> allPiles = new ArrayList<>();
        Collection<Enum<P>> categories = patience.getPileCategories();
        Collection<Enum<P>> names = patience.getPileNames();
        for (Enum<P> categoryOrName : categoriesOrNames) {
            if (categories.contains(categoryOrName)) {
                allPiles.addAll(patience.getPiles(categoryOrName));
            } else if (names.contains(categoryOrName)) {
                allPiles.add(patience.getPile(categoryOrName));
            }
        }
        return allPiles;
    }

    public static Collection<PileView> createPileViews(Collection<Pile> piles) {
        return piles.stream().map(PileView::new).collect(Collectors.toList());
    }

    public static <P extends Enum<P>> Collection<PileView> createPileViews(Patience<P> patience, Enum<P> pileCategory) {
        return createPileViews(patience.getPiles(pileCategory));
    }

    public static <T> void setPileViewProperties(Function<PileView, WritableValue<T>> fun, T value, Iterable<PileView> pileViews) {
        for (var pileView : pileViews) {
            WritableValue<T> pileViewProp = fun.apply(pileView);
            pileViewProp.setValue(value);
        }
    }
}