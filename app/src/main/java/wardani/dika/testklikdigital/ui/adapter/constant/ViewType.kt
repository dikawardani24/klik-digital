package wardani.dika.testklikdigital.ui.adapter.constant

enum class ViewType(val value: Int) {
    DATA(1),
    NO_MORE_DATA(2),
    ERROR_OCCURED(3),
    LOADING(4);

    companion object {

        fun toPageViewType(type: Int): ViewType {
            var found = ERROR_OCCURED
            for (pageViewType in values()) {
                if (pageViewType.value == type) {
                    found = pageViewType
                    break
                }
            }

            return found
        }
    }
}