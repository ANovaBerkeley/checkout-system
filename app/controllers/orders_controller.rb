class OrdersController < ApplicationController
  before_action :set_order, only: [:show, :edit, :update, :destroy]

  require 'rqrcode'
  require 'qrio'

  def index
    @orders = Order.all
    @active = Order.active?
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
    flash[:notice] = "Item marked as returned. Thank you!"
    redirect_to orders_path

    begin
      OrderMailer.delay.return_order(@order, @current_user).deliver
    rescue Exception => e
    end
  end

  def new
    @order = Order.new
    @student = Student.all
  end

  def scan
    render :scan
  end

  def scan_student
    item = Item.find_by_upc(params[:upc][0...-1])
    if item.nil?
      flash[:alert] = 'Unsuccessful scan.'
      redirect_to :back
    elsif item.remaining_quantity <= 0
      flash[:alert] = 'The quantity you entered is not currently available'
      redirect_to items_path
    else
      redirect_to confirm_item_path(:id => item.id)
    end
  end

  def create_barcode_order
    student_upc = params[:upc]
    item_id = params[:item_id].strip
    student = Student.find_by_upc(params[:upc][0...-1])

    if student.nil?
      flash[:alert] = 'Unsuccessful Scan. Please proceed manually.'
      redirect_to new_order_path(item: item_id)
      return
    end

    params[:order] = Hash.new
    params[:order][:item_id] = item_id
    params[:order][:quantity] = 1
    params[:order][:student_id] = student.id
    params[:order][:expire_at] = DateTime.new(2018,3,17)

    params[:order][:status] = true
    @order = Order.new(order_params)
    if @order.save
      @current_user = current_user
      @borrowed_item = Item.find_by_id(params[:order][:item_id])
      @borrowed_item.decrement!(:remaining_quantity, params[:order][:quantity].to_i)
      flash[:notice] = 'Order was successfully created.'
      flash.keep(:notice)
      redirect_to orders_path
      begin
        OrderMailer.delay.create_order(@order, @current_user).deliver
      rescue Exception => e
      end
    else
      flash[:alert] = 'Unsuccessful Scan. Please proceed manually.'
      redirect_to new_order_path(item: item_id)
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
        redirect_to orders_path, notice: 'Order was successfully created.'
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

  def get_students
    Student.all.map do |student| [student_id, student]
    end
  end

  def get_mentors
    Mentor.all.map do |mentor| [mentor_id, mentor]
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
      params.require(:order).permit(:quantity, :expire_at, :status, :item_id, :student_id)
    end
end
