class OrdersController < ApplicationController
  before_action :set_order, only: [:show, :edit, :update, :destroy]

  require 'rqrcode'
  require 'qrio'

  def index
    @orders = Order.all
    @members = Member.all
    @items = Item.all
    @active = Order.active?
    @expired = Order.expired?
  end

  def old
    @inactive = Order.inactive?
  end

  def renew
    @current_user = current_user
    @order = Order.find_by_id(params[:id])
    Order.renew(params[:id])
    redirect_to :root
    flash[:notice] = "Renewed for 7 days from now. Enjoy!"

    begin
      OrderMailer.delay.renew_order(@order, @current_user).deliver
    rescue Exception => e
    end
  end

  def disable
    borrowed_qty = Order.find_by_id(params[:id]).quantity.to_i
    @borrowed_item = Order.find_by_id(params[:id]).item
    @borrowed_item.increment!(:remaining_quantity, borrowed_qty)
    @current_user = current_user
    @order = Order.find_by_id(params[:id])
    Order.disable(params[:id])
    redirect_to :root
    flash[:notice] = "Item marked as returned. Thank you!"

    begin
      OrderMailer.delay.return_order(@order, @current_user).deliver
    rescue Exception => e
    end
  end

  def new
    @order = Order.new
    @member = Member.all
  end

  def scan
    render :scan
  end

  def scan_member
    # TODO: figure out why page cuts off when navigating from sidebar
    puts 'hello'
    puts params[:upc][0...-1]
    item = Item.find_by_upc(params[:upc][0...-1])
    # TODO: error handling
    if item.nil?
      puts 'do something here'
    else
      # item_id = Item.first.id
      redirect_to item_path(:id => item.id)
    end
  end

  def create_barcode_order
    # TODO: figure out how to get member from barcode
    member_upc = params[:upc]
    puts 'hello 74'
    puts member_upc
    item_id = params[:item_id]
    member = Member.find_by_upc(params[:upc][0...-1])

    # TODO: error handling

    # TODO: figure out why notices and alerts aren't working

    params[:order] = Hash.new
    params[:order][:item_id] = params[:item_id]
    params[:order][:quantity] = 1
    params[:order][:member_id] = member.id
    params[:order][:expire_at] = DateTime.new(2018,3,21)
    
    if Item.find_by_id(params[:item_id]).remaining_quantity >= params[:order][:quantity].to_i
      params[:order][:status] = true
      @order = Order.new(order_params)
      if @order.save
        @current_user = current_user
        @borrowed_item = Item.find_by_id(params[:order][:item_id])
        @borrowed_item.decrement!(:remaining_quantity, params[:order][:quantity].to_i)
        redirect_to :root, notice: 'Order was successfully created.'
        begin
          OrderMailer.delay.create_order(@order, @current_user).deliver
        rescue Exception => e
        end
      else
        render :new
      end
    else
      flash[:alert] = 'The quantity you entered is not currently available'
      redirect_to :root
    end
  end

  def create
    if Item.find_by_id(params[:order][:item_id]).remaining_quantity >= params[:order][:quantity].to_i
      params[:order][:status] = true
      @order = Order.new(order_params)
      if @order.save
        @current_user = current_user
        @borrowed_item = Item.find_by_id(params[:order][:item_id])
        @borrowed_item.decrement!(:remaining_quantity, params[:order][:quantity].to_i)
        redirect_to :root, notice: 'Order was successfully created.'
        begin
          OrderMailer.delay.create_order(@order, @current_user).deliver
        rescue Exception => e
        end
      else
        render :new
      end
    else
      flash[:alert] = 'The quantity you entered is not currently available'
      redirect_to :back
    end
  end

  def get_members 
    Member.all.map do |member| [member_id, member] 
    end
  end

  def destroy
    borrowed_qty = @order.quantity.to_i
    @borrowed_item = @order.item
    @borrowed_item.increment!(:remaining_quantity, borrowed_qty)
    @current_user = current_user
    @order.destroy

    redirect_to orders_url, notice: 'Order was successfully destroyed.'
    begin
      OrderMailer.delay.cancel_order(@order, @current_user).deliver
    rescue Exception => e
    end
  end

  private
    def set_order
      @order = Order.find(params[:id])
    end

    def order_params
      params.require(:order).permit(:quantity, :expire_at, :status, :item_id, :member_id)
    end
end
