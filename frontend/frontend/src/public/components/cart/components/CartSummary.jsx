import '../../../styles/CartSummary.css'
export const CartSummary = ({ total, onClear }) => (
  <div className="cart-summary mt-4 text-right">
    <h4>Total: ${total.toFixed(2)}</h4>

   
    <div className="flex justify-end gap-2 mt-2">
      <button onClick={onClear} className="btn btn-outline-secondary clear-button">Vaciar</button>
      <button className="btn btn-success checkout-button">Continuar al checkout</button>
    </div>

     
  </div>
);
