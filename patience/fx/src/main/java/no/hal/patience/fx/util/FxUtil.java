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

    public static List<Pile> getAllPiles(Patience patience, String... categoriesOrNames) {
        List<Pile> allPiles = new ArrayList<>();
        Collection<String> categories = patience.getPileCategories();
        Collection<String> names = patience.getPileNames();
        for (String categoryOrName : categoriesOrNames) {
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

    public static Collection<PileView> createPileViews(Patience patience, String pileCategory) {
        return createPileViews(patience.getPiles(pileCategory));
    }

    public static <T> void setPileViewProperties(Function<PileView, WritableValue<T>> fun, T value, Iterable<PileView> pileViews) {
        for (var pileView : pileViews) {
            WritableValue<T> pileViewProp = fun.apply(pileView);
            pileViewProp.setValue(value);
        }
    }
}