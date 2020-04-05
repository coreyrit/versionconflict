package singlesort.component;

import versionconflict.Card;

public class MatchedComponent {
    private Component component;

    public MatchedComponent(Component component) {
        this.component = component;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchedComponent that = (MatchedComponent) o;
        if(this.component.getMaterial() != that.component.getMaterial()) {
            return false;
        }

        switch(component.getMaterial()) {
            case Cardboad:
                Cardboard cb1 = (Cardboard)this.component;
                Cardboard cb2 = (Cardboard)that.component;
                return cb1.getColor().equals(cb2.getColor()) && cb1.getFace().isClean() == cb2.getFace().isClean() && cb1.getFace().getValue() == cb2.getFace().getValue();
            case Plastic:
                Plastic p1 = (Plastic)this.component;
                Plastic p2 = (Plastic)that.component;
                return p1.getColor().equals(p2.getColor()) && p1.getFace().getValue() == p2.getFace().getValue();
            case Glass:
                return this.component.getColor().equals(that.component.getColor());
            case Metal:
                Metal m1 = (Metal)this.component;
                Metal m2 = (Metal)that.component;
                return m1.getType() == m2.getType();
        }
        return false;
    }

    @Override
    public int hashCode() {
        if(component == null) {
            return 0;
        } else {
            switch (component.getMaterial()) {
                case Cardboad:
                    return ((Cardboard) component).getFace().hashCode() + component.getColor().hashCode();
                case Plastic:
                    return ((Plastic) component).getFace().hashCode() + component.getColor().hashCode();
                case Glass:
                    return component.getColor().hashCode();
                case Metal:
                    return ((Metal) component).getType().hashCode();
            }
            return 0;
        }
    }
}
